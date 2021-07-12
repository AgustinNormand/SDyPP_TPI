# Setup:
* wget https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
* kubectl create namespace argocd
* kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
* kubectl apply -n argocd -f app.yaml

# GUI:
* kubectl port-forward svc/argocd-server -n argocd 8080:443
* User: Admin
* Password: kubectl -n argocd get secrets argocd-initial-admin-secret -o jsonpath='{.data.password}' | base64 -d