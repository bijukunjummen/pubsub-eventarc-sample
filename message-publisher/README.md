
## Start the pubsub emulator locally
```shell
gcloud beta emulators pubsub start --project=sampleproj --host-port=localhost:8934
```


## Create topic and subscription
```shell
gcloud config configurations create emulator
gcloud config set auth/disable_credentials true
gcloud config set project sampleproj
gcloud config set api_endpoint_overrides/pubsub http://localhost:8934/

export PUBSUB_EMULATOR_HOST=localhost:8934
export PUBSUB_PROJECT_ID=sampleproj

# These don't work with the emulator! The application takes care of creating the topic and the subscription
#gcloud pubsub topics create sample-topic
#gcloud pubsub subscriptions create sample-subscription --topic=sample-topic
```

## Start the application
```shell
../gradlew bootRun 
```

## Publish
```shell
curl -v \
  -H "Content-type: application/json" \
  -H "Accept: application/json" \
   http://localhost:8080/messages \
   -d '{
   "id": "test-id",
   "payload": "sample-payload"
}'
```

Content should get published to a topic.
The application subscribes to the topic and prints the content to the console
```shell
[ault-executor-0] org.bk.service.MessageProcessor          : Processing message: Message[id=test-id-100, payload=sample-payload]
[ault-executor-0] org.bk.service.MessageProcessor          : Processing message: Message[id=test-id-100, payload=sample-payload]
[ault-executor-0] org.bk.service.MessageProcessor          : Processing message: Message[id=test-id-100, payload=sample-payload]
[ault-executor-0] org.bk.service.MessageProcessor          : Processing message: Message[id=test-id-100, payload=sample-payload]
[ault-executor-0] org.bk.service.MessageProcessor          : Processing message: Message[id=test-id-100, payload=sample-payload]
[ault-executor-0] org.bk.service.MessageProcessor          : Processing message: Message[id=test-id-100, payload=sample-payload]
[ault-executor-0] org.bk.service.MessageProcessor          : Processing message: Message[id=test-id-100, payload=sample-payload]
```