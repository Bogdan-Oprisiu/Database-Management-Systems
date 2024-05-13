from flask import Flask, request, jsonify
from sqlalchemy import create_engine, text

app = Flask(__name__)


class UserServicePython:
    def __init__(self):
        self.engine = create_engine('mysql://root:password@localhost/dbms_lab', isolation_level='READ_UNCOMMITTED')


userService = UserServicePython()


@app.route('/dirty-write-python', methods=['POST'])
def transaction2_dirty_write():
    data = request.get_json()
    user_id = data.get('user_id')

    if user_id is None:
        return jsonify({'error': 'user_id is required'}), 400

    try:
        with userService.engine.connect() as connection:
            query = text("UPDATE user SET first_name = 'Josh' WHERE user_id = :user_id")
            connection.execute(query, {"user_id": user_id})
            connection.commit()
        return jsonify({'success': True}), 200
    except Exception as e:
        return jsonify({'error': str(e)}), 500


if __name__ == '__main__':
    app.run(debug=True)
