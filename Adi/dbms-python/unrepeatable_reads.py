# from flask import Flask, request, jsonify
# from sqlalchemy import create_engine, text
#
# app = Flask(__name__)
#
#
# class WorkoutServicePython:
#     def __init__(self):
#         self.engine = create_engine('mysql://root:password@localhost/dbms_lab', isolation_level='READ_COMMITTED')
#
#
# workoutService = WorkoutServicePython()
#
#
# @app.route('/unrepeatable-reads-python', methods=['POST'])
# def transaction2_unrepeatable_reads():
#     data = request.get_json()
#     workout_id = data.get('workout_id')
#
#     if workout_id is None:
#         return jsonify({'error': 'workout_id is required'}), 400
#
#     try:
#         with workoutService.engine.connect() as connection:
#             # perform an update to modify the data between the two reads from the java code
#             query = text("UPDATE workout SET duration = 60 WHERE workout_id = :workout_id")
#             connection.execute(query, {"workout_id": workout_id})
#             connection.commit()
#         return jsonify({'modified_duration': 60}), 200
#     except Exception as e:
#         return jsonify({'error': str(e)}), 500
#
#
# if __name__ == '__main__':
#     app.run(debug=True)
