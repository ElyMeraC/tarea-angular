import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { IonicModule, ToastController } from '@ionic/angular';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { ContactService } from '../services/contact.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-crear-contacto',
  templateUrl: 'crear-contacto.page.html',
  styleUrls: ['crear-contacto.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, ReactiveFormsModule],
})
export class CrearContactoPage {
  contactoForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private contactService: ContactService,
    private auth: AuthService,
    private toastCtrl: ToastController,
    private router: Router
  ) {
    this.contactoForm = this.fb.group({
      nombre: ['', Validators.required],
      telefono: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
    });
  }

  async onCreate() {
    if (this.contactoForm.invalid) {
      this.contactoForm.markAllAsTouched();
      return;
    }

    const owner = this.auth.getCurrentUser();
    if (!owner) {
      await this.presentToast('Debes iniciar sesión primero');
      await this.router.navigateByUrl('/home', { replaceUrl: true });
      return;
    }

    const { nombre, telefono, email } = this.contactoForm.value;
    try {
      const response = await firstValueFrom(this.contactService.createContact(nombre, telefono, email, owner));
      const toast = await this.toastCtrl.create({
        message: response.message,
        duration: 2500,
        position: 'bottom',
      });
      await toast.present();

      if (response.state) {
        this.router.navigateByUrl('/mis-contactos', { replaceUrl: true });
      }
    } catch (error: any) {
      await this.presentToast(error?.message || 'Error al crear el contacto');
    }
  }

  private async presentToast(message: string) {
    const toast = await this.toastCtrl.create({
      message,
      duration: 2500,
      position: 'bottom',
    });
    await toast.present();
  }
}
