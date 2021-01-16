from flask import Flask, request
from flask_restful import Api, Resource, reqparse
import json
from pathlib import Path
import os.path

application = Flask(__name__)
api = Api(application)


parser = reqparse.RequestParser()
another_parser = reqparse.RequestParser()

#Read all the reviews from a persistent file
def retrieve_reviews():
    buffer_list = []
    flag = False
    if os.path.isfile('reviews.txt') :
        flag = True
    else:
        flag = False
    if(flag):
        with open('reviews.txt', 'r') as reviews_db:
            buffer_list = json.load(reviews_db)
    print(buffer_list)
    return buffer_list

reviews_list = retrieve_reviews()     #list of the form [[review], [review], ...] each review has the form [user_email, tour_id, review_score, review_message]

class Server(Resource):

    def get(self):
        #REQ is the id of the request
        another_parser.add_argument('nametour', required = True)
        args = another_parser.parse_args()

        nametour = args['nametour'] #LA RICHIESTA CLIENT AVRA PARAMETRO TOURID

        average_rating, reviews_number = get_number_rating(nametour)
        print(average_rating, reviews_number)
        
        return {'msg': "GET", 'error': False, 'rating': average_rating, 'number': reviews_number}



    def post(self):
        parser.add_argument('email', required = True)
        parser.add_argument('nametour', required=True)
        parser.add_argument('rate', type=float, required = True)
        args = parser.parse_args()


        email = args['email'] #LA RICHIESTA CLIENT AVRA PARAMETRO EMAIL
        nametour = args['nametour'] #IL NOME DEL TOUR DELLA RICHIESTA POST
        rate = args['rate']#IL VOTO DATO DAL CLIENT
        comment = request.get_json(force=True)['Comment']#IL COMMENTO DATO DAL CLIENT

        new_review(email, nametour, rate, comment)

        return {'msg': "POST", 'error': False}

    
    def delete(self):

        #REQ is the id of the request
        another_parser.add_argument('nametour', required = True)
        args = another_parser.parse_args()

        nametour = args['nametour'] #LA RICHIESTA CLIENT AVRA PARAMETRO TOURID

        review_tour_delete(nametour)
        print(reviews_list)
        
        return {'msg': "DELETE", 'error': False}


#Add a review (done with the post)
def new_review (email, nametour, rate, message):
    buffer_list = [email, nametour, rate, message]
    index_duplicate = -1
    for i in range(len(reviews_list)):
        if ((reviews_list[i][0] == buffer_list[0]) and (reviews_list[i][1] == buffer_list[1])):
            index_duplicate = i
    if (index_duplicate != -1):
        del reviews_list[index_duplicate]
    reviews_list.append(buffer_list)
    print(reviews_list)
    store_reviews();

#Return a rating and the number of reviews for a tour (done with the get)
def get_number_rating(nametour):
    sum_of_reviews_rating = 0
    total_number_of_reviews = 0
    for i in range(len(reviews_list)):
        if (reviews_list[i][1] == nametour):
            sum_of_reviews_rating += reviews_list[i][2]
            total_number_of_reviews += 1
    average_rating = sum_of_reviews_rating/total_number_of_reviews
    return average_rating, total_number_of_reviews

#Delete all the reviews of one tour (done with the delete)
def review_tour_delete(nametour):
    for i in range(len(reviews_list)):
        if (reviews_list[i][1] == nametour):
            del reviews_list[i]
    print(reviews_list)
    store_reviews();

#Write all the reviews on a file to get them persistent
def store_reviews():
    with open('reviews.txt', 'w') as reviews_db:
        json.dump(reviews_list, reviews_db)


api.add_resource(Server, '/')

if __name__ == '__main__':
    print('starting api....')
    application.run(host='0.0.0.0')
