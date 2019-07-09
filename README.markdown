# Collections vs Streams

## Notas sobre composabilidad de Streams

https://chrisdone.com/posts/stream-composability/

## Otras notas

https://stackoverflow.com/questions/35150231/java-streams-lazy-vs-fusion-vs-short-circuiting

## Simple bench

### carga

```bash
#!/bin/bash
if [ "$METHOD" != "streaming" ] && [ "$METHOD" != "buffering" ]
then
  >&2 echo "ERROR: varenv METHOD should be 'streaming' or 'buffering'"
  exit 1
fi
if [ "$URL" == "" ]
then
  URL="http://localhost:8080"
  >&2 echo "WARN: setting up URL=$URL"
fi
if [ "$N" == "" ]
then
  N=5000000
  >&2 echo "WARN: setting up N=$N"
fi
if [ "$SIZE" == "" ]
then
  SIZE=10000
  >&2 echo "WARN: setting up SIZE=$SIZE"
fi
for i in `seq 1 200`
do
  IX=$((RANDOM % (N - SIZE)))
  curl -s "$URL/$METHOD?from=`printf "%07i" $IX`&to=`printf "%07i" $((IX + SIZE))`" | tr ',' '\n' | wc -l
done
echo "done"
```

### run

```bash
for i in `seq 1 30`; do METHOD=buffering SIZE=100000 ./run & done
```

### results

![memory](mem.PNG)
![throughput](throughput.PNG)

### usando directamente mongodb (sin trabajo de negocio)

![mongodb](mongo.PNG)

### comparando escritura raw con serialización vía streaming

![raw_vs_stream_transactiontime](raw_vs_stream_transactiontime.PNG)
![raw_vs_stream_throughput](raw_vs_stream_throughput.PNG)
![raw_vs_stream_slow](raw_vs_stream_slow.PNG)
![raw_vs_stream_mem](raw_vs_stream_mem.PNG)
