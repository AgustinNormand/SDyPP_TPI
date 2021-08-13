get_value () {
    echo $(cat variables.tf | grep $1 -A 4 | grep default | awk {'print $3'} | sed 's/"//g') 
}

RECORD_SETS=$(gcloud dns record-sets list --zone $(get_value "dns_zone_name") | tail -n +2 | awk {'print $1"&"$2'})

for RECORD_SET in $RECORD_SETS ; do
    RECORD_ARRAY=(${RECORD_SET//&/ })
    RECORD=${RECORD_ARRAY[0]}
    TYPE=${RECORD_ARRAY[1]}
    if [ $TYPE == A ] || [ $TYPE == TXT ]
    then
        gcloud dns record-sets delete $RECORD --zone $(get_value "dns_zone_name") --type $TYPE
    fi
done

gcloud dns managed-zones delete $(get_value "dns_zone_name")