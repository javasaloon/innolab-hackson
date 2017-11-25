import pandas as pd
xls = pd.ExcelFile("/Users/i311682/github/innolab-hackson/parser/shops.xlsx")

sheetX = xls.parse(0) #2 is the sheet number

print sheetX.shape