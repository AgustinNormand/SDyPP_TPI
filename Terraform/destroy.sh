get_value () {
    echo $(cat variables.tf | grep $1 -A 4 | grep default | awk {'print $3'} | sed 's/"//g') 
}

RECORD_SETS=$(gcloud dns record-sets list --zone $(get_value "framework-services-gcp-com-ar") | tail -n +2 | awk {'print $1"&"$2'})

#RECORSET&TYPE

for RECORD_SET in $RECORD_SETS ; do
    #echo $RECORD_SET
    RECORD_ARRAY=(${RECORD_SET//&/ })
    RECORD=${RECORD_ARRAY[0]}
    TYPE=${RECORD_ARRAY[1]}
    #echo $RECORD
    #echo $TYPE
    if [ $TYPE == A]
    then
        echo "ENTERED HERE"
        #gcloud dns record-sets delete $RECORD --zone $(get_value "framework-services-gcp-com-ar") --type $TYPE
    fi

done