import os

from flask import Flask, request,render_template

app = Flask(__name__,template_folder='template')

# photos = UploadSet('pic',IMAGES)
app.config['UPLOADED_PHOTOS_DEST'] = 'WebServer/UploadImage'


@app.route('/RPotato')
def RPotato():
    return render_template('Potato.html')

@app.route('/RTomato')
def RTomato():
    return render_template('Tomato.html')


# configure_uploads(app,photos)
@app.route('/getPotato', methods=['GET', 'POST'])
def GetPotato():
    if request.method == 'POST':
        file = request.files['pic']
        filename = file.filename
        file.save(os.path.join(app.config['UPLOADED_PHOTOS_DEST'], filename))
        from keras.models import load_model
        import cv2
        import numpy as np
        import keras

        img = cv2.imread(os.path.join(app.config['UPLOADED_PHOTOS_DEST'], filename))
        img = cv2.resize(img, (256, 256))
        img = np.reshape(img, [1, 256, 256, 3])
        modelp = load_model('potatoCV.h5')
        modelp.compile(loss='binary_crossentropy', optimizer='adam', metrics=['accuracy'])

        classes = modelp.predict_classes(img)
        prediction = modelp.predict(img)
        dis = classes[0]
        pln = "ERROR"
        result = 0
        for y in prediction[0]:
            result += y
        if dis != (len(prediction[0]) - 1):
            result = prediction[0][dis] / result
            pln = "Unhealthy\n"
        else:
            result = prediction[0][dis] / result
            pln = "Healthy\n"
        keras.backend.clear_session()
        return pln + " " + "%.2f" % (result * 100)
    else:
        return "Y U NO USE POST?"





@app.route('/getTomato', methods=['GET', 'POST'])
def GetTomato():
    if request.method == 'POST':
        file = request.files['pic']
        filename = file.filename
        file.save(os.path.join(app.config['UPLOADED_PHOTOS_DEST'], filename))
        from keras.models import load_model
        import cv2
        import numpy as np
        import keras

        img = cv2.imread(os.path.join(app.config['UPLOADED_PHOTOS_DEST'], filename))
        img = cv2.resize(img, (256, 256))
        img = np.reshape(img, [1, 256, 256, 3])
        modelt = load_model('tomatoCV3_2.h5')
        modelt.compile(loss='binary_crossentropy', optimizer='adam', metrics=['accuracy'])
        classes = modelt.predict_classes(img)
        prediction = modelt.predict(img)
        dis = classes[0]
        pln = "ERROR"
        result = 0
        for y in prediction[0]:
            result += y
        if dis != len(prediction[0]-1):
            result = prediction[0][dis] / result
            pln = "Unhealthy\n"
        else:
            result = prediction[0][dis] / result
            pln = "Healthy\n"
        print(prediction)
        print(dis)
        keras.backend.clear_session()

        return pln + " " + "%.2f" %(result * 100)
    else:
        return "Y U NO USE POST?"






@app.route("/")
def main():
    return render_template('index.html')


# running web app in local machine
if __name__ == '__main__':
    app.run(host='...', port=)#in plasc of ... use your ip or domain name to host your server
