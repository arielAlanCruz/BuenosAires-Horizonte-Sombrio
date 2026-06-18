# 🧉 Buenos Aires: Horizonte Sombrío

Un videojuego RPG de combates por turnos desarrollado enteramente en **Java**. El proyecto está ambientado en el folclore argentino y la geografía bonaerense, combinando mecánicas clásicas de rol con elementos culturales locales.

## 📝 Descripción del Proyecto
El jugador controla a una "party" de cuatro héroes (Guerrero, Mago, Arquero y Curandera) que deben enfrentarse a diversas amenazas oscuras en las calles de Buenos Aires. El juego cuenta con un sistema de progresión de niveles, manejo de inventario compartido, habilidades especiales y un sistema de guardado de partidas.

Este proyecto fue diseñado con un fuerte enfoque en **Programación Orientada a Objetos (POO)** y arquitectura **Modelo-Vista-Controlador (MVC)**, garantizando un código escalable, modular y fácil de mantener.

## ✨ Características Principales
* **Combate por Turnos:** Sistema de turnos dinámico basado en las estadísticas de velocidad de cada entidad.
* **Clases y Habilidades:** Habilidades únicas por clase con cálculo de daño y curación, además de la posibilidad de aturdir enemigos o usar escudos.
* **Inventario Folclórico:** Uso de ítems consumibles (Torta Frita, Mate, Asado) y equipamiento interactivo (Poncho de Alpaca, Facón Criollo).
* **Persistencia de Datos:** Sistema de guardado y carga de partidas mediante serialización de objetos en Java.
* **Interfaz Gráfica (GUI):** Desarrollada con **Java Swing** y **AWT**, utilizando gráficos 2D y animaciones básicas para los ataques.

## 🏗️ Arquitectura (MVC)
El código está rigurosamente dividido para separar la lógica de negocio de la interfaz de usuario:
* **Modelo:** Define las reglas del juego (`MotorCombate`, jerarquía de `Personaje` y `Enemigo`, manejo de `Inventario` y `Habilidad`). Implementa el patrón Singleton para el estado global (`GameEngine`).
* **Controlador:** `ControladorJuego` actúa como orquestador, procesando las acciones del usuario, actualizando el modelo y coordinando las transiciones de pantalla.
* **Vista:** Interfaz gráfica construida con `JFrame` y paneles individuales (`PantallaBatalla`, `PantallaFogata`, etc.) gestionados a través de `CardLayout`.
* **DTOs:** Uso de Data Transfer Objects para enviar información del Modelo a la Vista sin acoplar las capas.

## 🛠️ Tecnologías Utilizadas
* **Lenguaje:** Java (JDK 8 o superior).
* **UI:** Java Swing, Abstract Window Toolkit (AWT).
* **Patrones de Diseño:** MVC, Singleton, GRASP(General Responsibility Assignment Software ),DTOs

## 🚀 Cómo ejecutar el proyecto
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/arielAlanCruz/BuenosAires-Horizonte-Sombrio.git
