from keytotext import pipeline

nlp = pipeline("k2t")

print(nlp(['monkey', 'wedding', 'food', 'Tokyo night']))