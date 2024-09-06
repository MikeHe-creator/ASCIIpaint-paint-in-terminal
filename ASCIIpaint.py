import tkinter
from tkinter import filedialog
from PIL import Image

ASCII_CHARS = [' ', '.', ':', '-', '=', '+', '*', '#', '%', '@']
MAX_WIDTH = 200  
MAX_HEIGHT =100 

def getimg():
    root = tkinter.Tk()
    root.withdraw()
    fileroute = tkinter.filedialog.askopenfilename(
        title="Select a picture",
        filetypes=[("Image files", "*.png;*.jpg;*.jpeg;"),
                   ("PNG files", "*.png"),
                   ("JPEG files", "*.jpg;*.jpeg")])
    root.destroy()
    return fileroute

def to_grayscale(img):
    return img.convert("L")

def resize_image(img):
    
    width, height = img.size
    
    ratio = min(MAX_WIDTH / width, MAX_HEIGHT / height)
    new_size = (int(width * ratio), int(height *ratio))
    
    return img.resize(new_size)

def pixel_to_ascii(img):
    pixels = img.getdata()
    ascii_str = "".join([ASCII_CHARS[pixel * (len(ASCII_CHARS) - 1) // 255] for pixel in pixels])
    return ascii_str

def image_to_ascii(fileroute):
    img = Image.open(fileroute)
    img = to_grayscale(img)
    img = resize_image(img)  
    imgwidth, imgheight = img.size
    ascii_str = pixel_to_ascii(img)

   
    ascii_str_len = len(ascii_str)
    ascii_img = "\n".join([ascii_str[i:(i + imgwidth)] for i in range(0, ascii_str_len, imgwidth)])
    return ascii_img

if __name__ == "__main__":
    fileroute = getimg()  
    if fileroute:
        ascii_image = image_to_ascii(fileroute)
        print(ascii_image)