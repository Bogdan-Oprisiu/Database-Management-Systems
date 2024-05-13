# from flask import Flask, jsonify
# from sqlalchemy import create_engine, text
#
# app = Flask(__name__)
#
#
# class ExerciseServicePython:
#     def __init__(self):
#         self.engine = create_engine('mysql://root:password@localhost/dbms_lab', isolation_level='READ_COMMITTED')
#
#
# exerciseService = ExerciseServicePython()
#
#
# @app.route('/phantom-read-python', methods=['POST'])
# def transaction2_phantom_read():
#     try:
#         with exerciseService.engine.connect() as connection:
#             query = text("INSERT INTO exercise (exercise_id, difficulty_level,  name, category_id, equipment_id) VALUES (3, 2, 'Bicep Curls', 1, 1)")
#             connection.execute(query)
#             connection.commit()
#         return jsonify({'success': True}), 200
#     except Exception as e:
#         return jsonify({'error': str(e)}), 500
#
#
# if __name__ == '__main__':
#     app.run(debug=True)
