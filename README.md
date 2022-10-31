Shows how to use a simple Kafka Streams Topology to rename a set of topics by removing their prefix, leveraging:
* the `.stream(Pattern.compile("..."))` builder pattern
* using a `TopicNameExtractor` (which falls down to a simple functions taking key, value, and record context as input parameters and returning a String as output)
* testing this using `TopologyTestDriver`

