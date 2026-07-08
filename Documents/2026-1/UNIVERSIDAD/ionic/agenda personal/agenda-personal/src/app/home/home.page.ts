import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { IonicModule, ToastController } from '@ionic/angular';
import { Router, RouterModule } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, ReactiveFormsModule, RouterModule],
})
export class HomePage {
  loginForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router,
    private toastCtrl: ToastController
  ) {
    this.loginForm = this.fb.group({
      usuario: ['', Validators.required],
      clave: ['', Validators.required],
      remember: [true],
    });
  }

  async onLogin() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const { usuario, clave, remember } = this.loginForm.value;
    try {
      const response = await firstValueFrom(this.auth.login(usuario, clave));
      if (response.state) {
        this.auth.setCurrentUser(usuario, remember);
        await this.router.navigateByUrl('/mis-contactos', { replaceUrl: true });
      } else {
        await this.presentToast(response.message || 'Usuario o clave incorrectos');
      }
    } catch (error: any) {
      await this.presentToast(error?.message || 'Error de conexión con el servidor');
    }
  }

  private async presentToast(message: string) {
    const toast = await this.toastCtrl.create({
      message,
      duration: 3000,
      position: 'bottom',
    });

    await toast.present();
  }
}
