#!/bin/bash

set -e

GC_PROJECT=loklak-201451036
GC_CLUSTER=loklak-cluster
TAG=gcr.io/$GC_PROJECT/loklak:$TRAVIS_COMMIT

# Take care of encrypted gz
echo ">>> Decrypting credentials"
openssl aes-256-cbc -K $encrypted_bc7e535763c0_key -iv $encrypted_bc7e535763c0_iv -in credentials.tar.gz.enc -out credentials.tar.gz -d
tar -xzf credentials.tar.gz

echo ">>> Removing onselete files"
sudo rm -f /usr/bin/git-credential-gcloud.sh
sudo rm -f /usr/bin/bq
sudo rm -f /usr/bin/gsutil
sudo rm -f /usr/bin/gcloud

echo ">>> Installing Google Cloud SDK with Kubernetes"
export CLOUDSDK_CORE_DISABLE_PROMPTS=1
curl https://sdk.cloud.google.com | bash > /dev/null  # Too noisy
source ~/google-cloud-sdk/path.bash.inc
gcloud components install kubectl

echo ">>> Authenticating Google Cloud using decrypted credentials"
gcloud auth activate-service-account --key-file client-secret.json

echo ">>> Configuring Google Cloud"
gcloud config set compute/zone us-central1-a
export GOOGLE_APPLICATION_CREDENTIALS=client-secret.json
gcloud config set project $GC_PROJECT
gcloud container clusters get-credentials $GC_CLUSTER


echo ">>> Tagging and pushing image $TAG"
docker tag loklak_server $TAG
gcloud docker -- push $TAG

echo ">>> Updating Kubernetes deployment"
kubectl set image deployment/loklak --namespace=default loklak=$TAG
