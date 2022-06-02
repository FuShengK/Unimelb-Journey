import pandas as pd ; import string as s ; from fuzzywuzzy.fuzz import token_set_ratio as ft
from nltk.corpus import stopwords as st ; from nltk.stem import PorterStemmer as ps
from nltk.stem import WordNetLemmatizer as wl ; import re
# Mainly to purify words
def pure(words):
    words = words.lower().translate(str.maketrans('', '', s.punctuation))
    tokens = words.split() ; stp = st.words('english')
    return " ".join([wl().lemmatize(ps().stem(i)) for i in tokens if not i in stp])

abt = pd.read_csv("abt_small.csv", encoding='ISO-8859-1')
buy = pd.read_csv("buy_small.csv", encoding='ISO-8859-1')
check = [] ; pair = dict() ; possible = dict()
# Start my comparing and filtering process
for a_id, a_n, a_des in zip(abt['idABT'], abt['name'], abt['description']):  
    ap = pure(a_n) ; adp = pure(a_des) ; at = ap.split()
    for b_id, b_n, bmf in zip(buy['idBuy'], buy['name'], buy['manufacturer']):
        bp = pure(b_n) ; bmp = pure(bmf) ; bt = bp.split()
        # To ignore some noise data and include if under these conditions
        if ((bmp in ap) or (bt[0] in ap) or (at[0] in bp)):
            if at[-1] in bp or bt[-1] in at[-1] or bmp in at[-1]:
                pair[a_id] = b_id
                break  
            for word in bp.split():
                mat1 = re.search(r"[a-z][0-9]", word) ; mat2 = re.search(r"\d{4}" , word)
                if mat1 and (ft(word, adp) >= 95 or (mat2 and mat2[0] in at[-1])):
                    pair[a_id] = b_id
                    break
                mat3 = re.search(r"\w{2}\d{2}", word)
                if (mat3 and word in at[-1]):
                    pair[a_id] = b_id
                    break
            score = ft(ap, bp) ; la = ft(at[-1], bt[-1]) ; al = ft(a_n.split()[-1], b_n.split()[-1])
            if la >= 92 or score >= 91 or al >= 78:
                if a_n not in possible:
                    possible[a_n] = [(score, a_id, b_id)]
                else:
                    possible[a_n].append((score, a_id, b_id))
        
# pair the highest similarity ratio item
for product in possible:
    picked = sorted(possible[product], reverse=True)[0]
    if picked[1] not in pair:
        pair[picked[1]] = picked[2]
# write in csv file          
pd.DataFrame({"idAbt":list(pair.keys()), "idBuy":list(pair.values())}).to_csv(r'task1a.csv', index = False)