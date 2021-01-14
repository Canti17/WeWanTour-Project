from flask import Flask
from flask_restful import Api, Resource, reqparse


application = Flask(__name__)
api = Api(application)

RATE = 1; #VALUE GIVEN TO THIS GET REQUEST
parser = reqparse.RequestParser()

class Server(Resource):

    def get(self):
        #REQ is the id of the request
        parser.remove_argument('rate')
        parser.add_argument('req', type=int, required = True)
        args = parser.parse_args()

        req = args['req'] #LA RICHIESTA CLIENT AVRA PARAMETRO REQ

        if req == RATE:
            return {'msg': "GET", 'error': False}



    def post(self):
        parser.add_argument('req', type=int, required = True)
        parser.add_argument('rate', type=float, required = True)
        args = parser.parse_args()

        req = args['req'] #LA RICHIESTA CLIENT AVRA PARAMETRO REQ
        rate = args['rate']#IL VOTO DATO DAL CLIENT?

        if rate > 5 or rate < 0:
            return {'msg': "POST", 'error': True}
        else:
            return {'msg': "POST", 'error': False}
            
        

api.add_resource(Server, '/')

if __name__ == '__main__':
    print('starting api....')
    application.run(host='0.0.0.0')
    
