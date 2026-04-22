from fastapi import FastAPI, UploadFile, File, Form
from PIL import Image
import io

from model import process

app = FastAPI()

@app.post("/predict")
async def predict(file: UploadFile = File(None), text: str = Form(None)):
    image = None

    if file:
        contents = await file.read()
        image = Image.open(io.BytesIO(contents)).convert("RGB")

    result = process(image=image, text=text, reports=3)

    return result