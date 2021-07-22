Cluster de resources levantado

Crear una service account que se llame "serviceaccount" con permisos de admin.

No crearle una clave, sino no funciona.

```bash
gcloud container clusters get-credentials gke-resources-cluster --zone us-central1 --project sdypp-316414
```

```bash
gcloud dns managed-zones create framework-services-gcp-com-ar \
    --dns-name=framework-services.gcp.com.ar. \
    --description="Automatically managed zone by ExternalDNS"
```

```bash
kubectl create namespace kubernetes-sa
```

```bash
kubectl create serviceaccount --namespace kubernetes-sa service-account
```

```bash
gcloud iam service-accounts add-iam-policy-binding \
  --role roles/iam.workloadIdentityUser \
  --member "serviceAccount:sdypp-316414.svc.id.goog[kubernetes-sa/service-account]" \
  serviceaccount@sdypp-316414.iam.gserviceaccount.com

```

```bash
kubectl annotate serviceaccount \
  --namespace kubernetes-sa \
  service-account \
  iam.gke.io/gcp-service-account=serviceaccount@sdypp-316414.iam.gserviceaccount.com

```


https://cloud.google.com/kubernetes-engine/docs/how-to/workload-identity#authenticating_to

https://github.com/kubernetes-sigs/external-dns/blob/master/docs/tutorials/gke.md


Me parece que no andaba, porque terraform no le da bola a la cuenta que le pongo en el main.tf. Crea unas nuevas. Y yo las habia borrado porque pensé que habian quedado ahi y ya no estaban siendo usadas. Entonces capaz se intentaba autenticar y no tenia ninguna cuenta. Habria que probarlo.