# -*- coding: utf-8 -*-
from PIL import Image
import os

# --- Configuración ---
input_icon_filename = 'favicon.ico'
output_specs = [
    {'filename': 'favicon-192x192.png', 'size': (192, 192)},
    {'filename': 'favicon-512x512.png', 'size': (512, 512)},
]
# --- Fin de la Configuración ---

def convertir_icono(input_path, specs):
    """
    Convierte un archivo .ico a varios archivos PNG con tamaños específicos.

    Args:
        input_path (str): La ruta al archivo favicon.ico de entrada.
        specs (list): Una lista de diccionarios, cada uno con 'filename' y 'size'.
                      Ej: [{'filename': 'out.png', 'size': (192, 192)}]
    """
    # Verificar si el archivo de entrada existe
    if not os.path.exists(input_path):
        print(f"Error: El archivo de entrada '{input_path}' no se encontró.")
        return

    try:
        # Abrir la imagen .ico original
        # Pillow normalmente selecciona la imagen de mayor resolución dentro del .ico
        print(f"Abriendo el archivo '{input_path}'...")
        with Image.open(input_path) as img:
            print(f"Archivo .ico abierto. Tamaño detectado (probablemente el más grande): {img.size}")

            # Procesar cada tamaño de salida especificado
            for spec in specs:
                output_filename = spec['filename']
                target_size = spec['size']

                print(f"Redimensionando a {target_size[0]}x{target_size[1]} para '{output_filename}'...")

                # Redimensionar la imagen. Image.Resampling.LANCZOS es bueno para calidad.
                # Si tu versión de Pillow es más antigua, podrías necesitar Image.LANCZOS o Image.ANTIALIAS
                try:
                    resized_img = img.resize(target_size, Image.Resampling.LANCZOS)
                except AttributeError:
                    # Fallback para versiones más antiguas de Pillow
                    print("Usando método de redimensionamiento ANTIALIAS (posible Pillow antiguo)")
                    resized_img = img.resize(target_size, Image.ANTIALIAS)


                # Guardar la imagen redimensionada en formato PNG
                print(f"Guardando '{output_filename}'...")
                resized_img.save(output_filename, 'PNG')
                print(f"'{output_filename}' guardado exitosamente.")

        print("\n¡Conversión completada!")

    except FileNotFoundError: # Aunque ya lo verificamos, por si acaso
        print(f"Error: El archivo de entrada '{input_path}' no se encontró durante el procesamiento.")
    except PermissionError:
         print(f"Error: Permiso denegado. No se pudo guardar uno o más archivos. Verifica los permisos del directorio.")
    except Exception as e:
        print(f"Ocurrió un error inesperado durante el procesamiento: {e}")

# --- Ejecutar la función ---
if __name__ == "__main__":
    convertir_icono(input_icon_filename, output_specs)
    
