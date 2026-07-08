import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { IonicModule, ToastController } from '@ionic/angular';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { ContactService, Contact } from '../services/contact.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-actualizar-contacto',
  templateUrl: 'actualizar-contacto.page.html',
  styleUrls: ['actualizar-contacto.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, ReactiveFormsModule],
})
export class ActualizarContactoPage {
  actualizarForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private contactService: ContactService,
    private auth: AuthService,
    private toastCtrl: ToastController,
    private router: Router
  ) {
    this.actualizarForm = this.fb.group({
      contactoId: ['', Validators.required],
      nombre: ['', Validators.required],
      telefono: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
    });

    const navigationState = this.router.getCurrentNavigation()?.extras?.state as { contact?: Contact } | undefined;
    const contact = navigationState?.contact ?? (window.history.state?.contact as Contact | undefined);
    if (contact) {
      this.actualizarForm.setValue({
        contactoId: contact.id,
        nombre: contact.nombre,
        telefono: contact.telefono,
        email: contact.email,
      });
    }
  }

  async onUpdate() {
    if (this.actualizarForm.invalid) {
      this.actualizarForm.markAllAsTouched();
      return;
    }

    const owner = this.auth.getCurrentUser();
    if (!owner) {
      await this.presentToast('Debes iniciar sesión primero');
      await this.router.navigateByUrl('/home', { replaceUrl: true });
      return;
    }

    const { contactoId, nombre, telefono, email } = this.actualizarForm.value;
    try {
      const response = await firstValueFrom(this.contactService.updateContact(Number(contactoId), nombre, telefono, email, owner));
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
      await this.presentToast(error?.message || 'Error al actualizar el contacto');
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
