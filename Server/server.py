from flask import Flask
from flask_restful import Api, Resource, reqparse


application = Flask(__name__)
api = Api(application)


parser = reqparse.RequestParser()
another_parser = reqparse.RequestParser()

class Server(Resource):

    def get(self):
        #REQ is the id of the request
        another_parser.add_argument('tourid', type=int, required = True)
        args = another_parser.parse_args()

        tourid = args['tourid'] #LA RICHIESTA CLIENT AVRA PARAMETRO TOURID

        
        return {'msg': "GET", 'error': False}



    def post(self):
        parser.add_argument('email', required = True)
        parser.add_argument('tourid',type=int, required=True)
        parser.add_argument('rate', type=float, required = True)
        args = parser.parse_args()

        email = args['email'] #LA RICHIESTA CLIENT AVRA PARAMETRO EMAIL
        tourid = args['tourid']  #IL TOUR ID DELLA RICHIESTA POST
        rate = args['rate']#IL VOTO DATO DAL CLIENT

        return {'msg': "POST", 'error': False}

    
    def delete(self):

        #REQ is the id of the request
        another_parser.add_argument('tourid', type=int, required = True)
        args = another_parser.parse_args()

        tourid = args['tourid'] #LA RICHIESTA CLIENT AVRA PARAMETRO TOURID

        
        return {'msg': "DELETE", 'error': False}

            
        

api.add_resource(Server, '/')

if __name__ == '__main__':
    print('starting api....')
    application.run(host='0.0.0.0')
    
