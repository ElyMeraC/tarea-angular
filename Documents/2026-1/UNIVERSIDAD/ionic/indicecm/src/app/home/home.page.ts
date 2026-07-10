import { Component } from '@angular/core';

interface ImcResult {
  valor: number;
  categoria: string;
  color: string;
  descripcion: string;
}

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  standalone: false,
})
export class HomePage {
  peso: number | null = null;
  altura: number | null = null;
  edad: number | null = null;
  genero: string = '';

  resultado: ImcResult | null = null;
  mostrarError: boolean = false;

  calcular() {
    if (!this.peso || !this.altura || !this.edad || !this.genero ||
        this.peso <= 0 || this.altura <= 0 || this.edad <= 0) {
      this.mostrarError = true;
      this.resultado = null;
      return;
    }

    this.mostrarError = false;
    const imc = this.peso / (this.altura * this.altura);

    if (imc < 18.5) {
      this.resultado = {
        valor: imc,
        categoria: 'Bajo Peso',
        color: 'bajo-peso',
        descripcion: 'Tu peso está por debajo del rango saludable. Consulta a un médico.'
      };
    } else if (imc < 25) {
      this.resultado = {
        valor: imc,
        categoria: 'Peso Normal',
        color: 'normal',
        descripcion: '¡Felicidades! Tu peso está en el rango saludable.'
      };
    } else if (imc < 30) {
      this.resultado = {
        valor: imc,
        categoria: 'Sobrepeso',
        color: 'sobrepeso',
        descripcion: 'Tu peso está por encima del rango saludable. Se recomienda actividad física.'
      };
    } else {
      this.resultado = {
        valor: imc,
        categoria: 'Obesidad',
        color: 'obesidad',
        descripcion: 'Tu peso representa un riesgo para la salud. Consulta a un médico.'
      };
    }
  }

  limpiar() {
    this.peso = null;
    this.altura = null;
    this.edad = null;
    this.genero = '';
    this.resultado = null;
    this.mostrarError = false;
  }
}
