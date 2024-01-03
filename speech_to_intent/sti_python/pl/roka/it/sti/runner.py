import json

print("printing something...")

result = json.loads('{"kind":"ConversationResult","result":{"query":"open the spotify please","prediction":{"topIntent":"openProgram","projectKind":"Conversation","intents":[{"category":"openProgram","confidenceScore":0.9489431},{"category":"nextTrack","confidenceScore":0.6016618},{"category":"closeProgram","confidenceScore":0.58726144},{"category":"None","confidenceScore":0}],"entities":[{"category":"program","text":"spotify","offset":9,"length":7,"confidenceScore":1}]}}}')['result']

print("result: ", result)
