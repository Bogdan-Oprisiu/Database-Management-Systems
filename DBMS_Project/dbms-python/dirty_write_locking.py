from flask import Flask, request, jsonify
from sqlalchemy import create_engine, text

app = Flask(__name__)


class UserServicePython:
    def __init__(self):
        self.engine = create_engine('mysql://root:password@localhost/dbms_lab', isolation_level='READ_UNCOMMITTED')


userService = UserServicePython()


@app.route('/dirty-write-locking-python', methods=['POST'])
def transaction2_dirty_write():
    data = request.get_json()
    user_id = data.get('user_id')

    if user_id is None:
        return jsonify({'error': 'user_id is required'}), 400

    try:
        with userService.engine.begin() as connection:
            # use FOR UPDATE to acquire a row-level lock on the user record
            query = text("SELECT * FROM _user WHERE user_id = :user_id FOR UPDATE")
            connection.execute(query, {"user_id": user_id})

            # update the user's first name
            query = text("UPDATE _user SET first_name = 'Jonathan' WHERE user_id = :user_id")
            connection.execute(query, {"user_id": user_id})

        return jsonify({'success': True}), 200
    except Exception as e:
        return jsonify({'error': str(e)}), 500


if __name__ == '__main__':
    app.run(debug=True)
