@echo off
pip install selenium
pip install chromedriver
pip install argparse
chcp 65001
C:
cd C:\Users\User\PycharmProjects\pythonScraping\ncz
python ncz_google_local_article.py %1 %2 %3

exit
