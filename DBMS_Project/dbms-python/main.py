import time

from flask import Flask, request, jsonify
from sqlalchemy import create_engine, text
from sqlalchemy.orm import sessionmaker

app = Flask(__name__)

# Database setup
DATABASE_URI = 'postgresql://postgres:bogdan123@postgres-db/computer_store'
# DATABASE_URI = 'postgresql://postgres:bogdan123@localhost/computer_store'
engine = create_engine(DATABASE_URI)
Session = sessionmaker(bind=engine)


@app.route('/dirty-write-python', methods=['POST'])
def dirty_write():
    session = Session()
    user_id = request.json.get('user_id')
    try:
        session.execute(text("UPDATE _user SET last_name = 'UpdatedDuringTransaction' WHERE user_id = :user_id"),
                        {'user_id': user_id})
        time.sleep(5)  # Simulate delay
        session.commit()
        return jsonify({'message': 'Dirty write completed successfully'}), 200
    except Exception as e:
        session.rollback()
        return jsonify({'error': str(e)}), 500
    finally:
        session.close()


@app.route('/lost-update-python', methods=['POST'])
def lost_update():
    session = Session()
    user_id = request.json.get('user_id')
    try:
        session.execute(text("UPDATE _user SET last_name = 'UpdatedDuringTransaction' WHERE user_id = :user_id"),
                        {'user_id': user_id})
        time.sleep(5)  # Simulate delay
        session.commit()
        return jsonify({'message': 'Dirty write completed successfully'}), 200
    except Exception as e:
        session.rollback()
        return jsonify({'error': str(e)}), 500
    finally:
        session.close()


@app.route('/simulate-update', methods=['POST'])
def simulate_update():
    session = Session()
    user_id = request.json['user_id']
    try:
        time.sleep(1)
        # Update the user
        session.execute(text("UPDATE _user SET last_name = 'UpdatedDuringTransaction' WHERE user_id = :user_id"),
                        {'user_id': user_id})
        # Hold the transaction open
        time.sleep(1)  # Ensure Java has time to read this uncommitted data
        # Then roll back the change
        session.rollback()
        return jsonify({'message': 'Update applied and then rolled back'}), 200
    except Exception as e:
        session.rollback()  # Ensure rollback on error
        return jsonify({'error': str(e)}), 500
    finally:
        session.close()



if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True, port=5000)
