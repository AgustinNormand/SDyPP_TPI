Redis cluster mode

https://rancher.com/blog/2019/deploying-redis-cluster/

$ kubectl exec -it redis-cluster-0 -- redis-cli --cluster create --cluster-replicas 1 $(kubectl get pods -l app=redis-cluster -o jsonpath='{range.items[*]}{.status.podIP}:6379 ')




EFK
https://www.digitalocean.com/community/tutorials/how-to-set-up-an-elasticsearch-fluentd-and-kibana-efk-logging-stack-on-kubernetes

https://cloud.google.com/kubernetes-engine/docs/concepts/persistent-volumes

https://github.com/argoproj/argocd-example-apps/blob/master/helm-dependency/README.md

https://medium.com/javarevisited/create-recording-rules-in-prometheus-8a6a1c0b9e11