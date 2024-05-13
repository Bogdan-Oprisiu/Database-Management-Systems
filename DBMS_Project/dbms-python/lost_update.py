from flask import Flask, request, jsonify
from sqlalchemy import create_engine, text

app = Flask(__name__)


class UserServicePython:
    def __init__(self):
        # PostgreSQL's connection string based on your YAML config
        self.engine = create_engine('postgresql://postgres:bogdan123@localhost/computer_store',
                                    isolation_level='READ_COMMITTED')


userService = UserServicePython()


@app.route('/lost-update-python', methods=['POST'])
def transaction2_lost_update():
    data = request.get_json()
    user_id = data.get('user_id')

    if user_id is None:
        return jsonify({'error': 'user_id is required'}), 400

    try:
        with userService.engine.connect() as connection:
            # simulate a delay to ensure it occurs after the first transaction commits its changes
            # time.sleep(5)

            # perform the update on the same user that the first transaction updated
            query = text("UPDATE _user SET last_name = 'Smith' WHERE user_id = :user_id")
            connection.execute(query, {"user_id": user_id})
            connection.commit()
        return jsonify({'success': True}), 200
    except Exception as e:
        return jsonify({'error': str(e)}), 500


if __name__ == '__main__':
    app.run(debug=True)
