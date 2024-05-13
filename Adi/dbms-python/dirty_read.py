# from flask import Flask, request, jsonify
# from sqlalchemy import create_engine, text
#
# app = Flask(__name__)
#
#
# class ExerciseServicePython:
#     def __init__(self):
#         self.engine = create_engine('mysql://root:password@localhost/dbms_lab', isolation_level='READ_UNCOMMITTED')
#
#
# exerciseService = ExerciseServicePython()
#
#
# @app.route('/dirty-read-python', methods=['POST'])
# def transaction2_dirty_read():
#     data = request.get_json()
#     exercise_id = data.get('exercise_id')
#
#     if exercise_id is None:
#         return jsonify({'error': 'exercise_id is required'}), 400
#
#     try:
#         with exerciseService.engine.connect() as connection:
#             # perform an update to modify the data between the two reads from the java code
#             query = text("UPDATE exercise SET difficulty_level = 2 WHERE exercise_id = :exercise_id")
#             connection.execute(query, {"exercise_id": exercise_id})
#
#             # fetch the modified difficulty level from the database
#             query_modified = text("SELECT difficulty_level FROM exercise WHERE exercise_id = :exercise_id")
#             result = connection.execute(query_modified, {"exercise_id": exercise_id}).fetchone()
#             if result:
#                 modified_difficulty = result[0]
#             else:
#                 return jsonify({'error': 'No data found for the given exercise_id'}), 404
#
#             # commit the changes but only after the retrieval of the updated difficulty level to ensure the dirty
#             # read of an uncommitted update
#             connection.commit()
#
#         return jsonify({'modified_difficulty': modified_difficulty}), 200
#     except Exception as e:
#         return jsonify({'error': str(e)}), 500
#
#
# if __name__ == '__main__':
#     app.run(debug=True)
