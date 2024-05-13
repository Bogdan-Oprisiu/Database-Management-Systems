import time

from flask import Flask, request, jsonify
from sqlalchemy import create_engine, text
from sqlalchemy.orm import sessionmaker

app = Flask(__name__)

# Database setup
DATABASE_URI = 'postgresql://postgres:bogdan123@localhost/computer_store'
engine = create_engine(DATABASE_URI)
Session = sessionmaker(bind=engine)


@app.route('/dirty-write-python', methods=['POST'])
def dirty_write():
    session = Session()
    user_id = request.json.get('user_id')
    try:
        session.execute(text("UPDATE _user SET last_name = 'DirtyWrite' WHERE user_id = :user_id"),
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
        session.begin()
        user = session.execute(text("SELECT * FROM _user WHERE user_id = :user_id FOR UPDATE"),
                               {'user_id': user_id}).fetchone()
        if user:
            time.sleep(5)
            session.execute(text("UPDATE _user SET last_name = 'LostUpdate' WHERE user_id = :user_id"),
                            {'user_id': user_id})
            session.commit()
            return jsonify({'message': 'Lost update issue demonstrated'}), 200
        else:
            session.rollback()
            return jsonify({'error': 'User not found'}), 404
    except Exception as e:
        session.rollback()
        return jsonify({'error': str(e)}), 500
    finally:
        session.close()



# Additional routes for other concurrency issues can be added in a similar fashion

if __name__ == '__main__':
    app.run(debug=True, port=5000)
