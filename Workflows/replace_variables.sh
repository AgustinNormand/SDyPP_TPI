TEMPLATE_FILENAME="template.yml"
ls . | grep .yml | while read FILE_NAME ; do
    if [ ! $FILE_NAME = $TEMPLATE_FILENAME ];then
        #echo $FILE_NAME
        cp $TEMPLATE_FILENAME $FILE_NAME'.copy'
        #cp $FILE_NAME $FILE_NAME'.copy'
        cat $FILE_NAME'' | grep "ENV_" | while read ENV_VARIABLE ; do
            KEY=$(echo $ENV_VARIABLE | sed 's/ENV_//g' | sed 's/:.*//g')
            VALUE=$(echo $ENV_VARIABLE | sed 's/ENV_//g' | sed 's/:/#firstdoublepoints#/1' | sed 's/.*#firstdoublepoints#//g' | sed 's/ //1')
            #echo $VALUE
            cat $FILE_NAME'.copy' | sed "s#\<$KEY\>#$VALUE#g">$FILE_NAME'.new'
            mv $FILE_NAME'.new' $FILE_NAME'.copy'
        done
        mv $FILE_NAME'.copy' ../.github/workflows/$FILE_NAME
    fi
done

    