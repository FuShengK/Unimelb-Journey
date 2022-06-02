import pandas as pd ; import nltk ; import string as s
from nltk.corpus import stopwords as st
from nltk.stem import PorterStemmer as ps
from nltk.stem import WordNetLemmatizer as wl
# Making block keys
def blocking(data, id_name, checking):
    stems = ps() ; block = dict()
    for data_id, check in zip(data[id_name], data[checking]):
        words = check.split() if type(check) != float else str(check)
        stw = st.words('english') ; sp = s.punctuation
        names = set([w for w in words if (not w in stw) and (not w in sp)])
        for word in names:
            stem = stems.stem(word)
            if stem not in block:
                block[stem] = [data_id]
            else:
                block[stem].append(data_id)
    return block
# To get key pairs
def keypair(block, data):
    keys = list() ; pairs = list()
    for key in list(block['key']):
        for pair in data[key]:
            keys.append(key) ; pairs.append(pair)
    return keys, pairs

abt = pd.read_csv("abt.csv", encoding='ISO-8859-1') ; key = "block_key"
buy = pd.read_csv("buy.csv", encoding='ISO-8859-1') ; p_id = "product_id"
abt_block = blocking(abt, 'idABT', 'name') ; a_csv = r'abt_blocks.csv'
buy_block = blocking(buy, 'idBuy', 'manufacturer') ; b_csv = r'buy_blocks.csv'

abt_block_key = pd.Series(list(abt_block.keys()), name='key')
buy_block_key = pd.Series(list(buy_block.keys()), name='key')
blocks = pd.merge(abt_block_key, buy_block_key, on='key', how='inner')
abd_keys, abd_id = keypair(blocks, abt_block)
buy_pairs, buy_id = keypair(blocks, buy_block)

# export to CSV
pd.DataFrame({key:abd_keys, p_id:abd_id}).to_csv(a_csv , index = False)
pd.DataFrame({key:buy_pairs, p_id:buy_id}).to_csv(b_csv , index = False)