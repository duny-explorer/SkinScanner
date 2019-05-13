from flask import Flask, request, jsonify
from keras.engine.saving import load_model
from keras.preprocessing.image import img_to_array
from PIL import Image
import numpy as np
import tensorflow as tf


model = None
graph = None


def init_model():  # Функция для загрузки модели
    global model, graph
    graph = tf.get_default_graph()  # Cохраняем граф по-умолчанию
    model = load_model("model")  # Загрузка основных сведений сетки


def processing_image(image):  # Функция для обработки изображения
    if image.mode != "RGB":  # Если формат картинки не RGB, то пределываем
        image = image.convent("RGB")

    image = np.expand_dims(img_to_array(image.resize((250, 250))), axis=0)  # Превращаем в массив numpy (250, 250, 3)
    return image


app = Flask(__name__)


@app.route('/result557578', methods=["POST"])
def load_file():
    if request.method == "POST":  # Необязательная проверка, т.к по данному адресу принимаются только POST запросы
        file = request.files["file"]  # Достаём файл, который пришёл
        if bool(file.filename):
            file.save("image.jpg")  # Сохраняем файл. Мне так удобнее и понятнее
            img = processing_image(Image.open("image.jpg"))  # Вызов функции для обработки фото
            with graph.as_default():  # В этом блоке происходит работа с графом сетки
                predict = model.predict(img)  # Предсказание модели

                return jsonify({"res": str(predict.argmax(1)[0])})  # Создание json файла с ответом для оправки обратно


if __name__ == '__main__':
    init_model()
    app.run(host='0.0.0.0')
