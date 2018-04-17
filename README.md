# filtering-functions

A [Pulsar Function](http://pulsar.apache.org/docs/latest/functions/quickstart/) that consumes JSON 
objects and filters based on a [jq](https://stedolan.github.io/jq/) query (specifically, using a [jackson module](https://github.com/eiiches/jackson-jq))

# Running the example

Start Pulsar

```bash
bin/pulsar standalone --advertised-address 127.0.0.1
```

Start the function

```bash
bin/pulsar-admin functions localrun \
  --jar ${project_home}/filtering-functions/build/libs/filtering-functions-1.0-SNAPSHOT-all.jar \
  --className org.robotninjas.functions.JQFilterFunction \
  --inputs persistent://sample/standalone/ns1/exclamation-input,persistent://sample/standalone/ns1/exclamation-input2 \
  --output persistent://sample/standalone/ns1/exclamation-output \
  --name json \
  --tenant standalone \
  --namespace ns1
```

Produce some data

```bash
bin/pulsar-client produce persistent://sample/standalone/ns1/exclamation-input \
  --num-produce 1000 \
  --messages '{"a":"c"}' \
  --rate 10
```

Consume the data

```bash
bin/pulsar-client consume persistent://sample/standalone/ns1/exclamation-output \
  --subscription-name my-subscription \
  --num-messages 0
```

Play with filters

```bash
bin/pulsar-client produce persistent://sample/standalone/ns1/exclamation-input2 \
  --num-produce 1 \
  --messages '.'
```

```
bin/pulsar-client produce persistent://sample/standalone/ns1/exclamation-input2 \
  --num-produce 1 \
  --messages '. | select(.a == "c") | .a'
```

Watch the output change

```bash
----- got message -----
[{"a":"c"}]
----- got message -----
[{"a":"c"}]
----- got message -----
[{"a":"c"}]
----- got message -----
[{"a":"c"}]
----- got message -----
[{"a":"c"}]
----- got message -----
[{"a":"c"}]
----- got message -----
[{"a":"c"}]
----- got message -----
[{"a":"c"}]
----- got message -----
[{"a":"c"}]
----- got message -----
[{"a":"c"}]
----- got message -----
[{"a":"c"}]
----- got message -----
[{"a":"c"}]
----- got message -----
["c"]
----- got message -----
["c"]
----- got message -----
["c"]
----- got message -----
["c"]
----- got message -----
["c"]
----- got message -----
["c"]
----- got message -----
["c"]
----- got message -----
["c"]
```
