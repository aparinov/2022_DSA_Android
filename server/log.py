import logging

logging.basicConfig(filename='logs.txt', encoding='utf-8',
                    format='%(levelname)s %(asctime)s %(filename)s %(funcName)s %(lineno)d %(message)s', level=logging.DEBUG)
