import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule, ToastController, AlertController } from '@ionic/angular';
import { RouterModule, Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { ContactService, Contact } from '../services/contact.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-mis-contactos',
  templateUrl: 'mis-contactos.page.html',
  styleUrls: ['mis-contactos.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, RouterModule],
})
export class MisContactosPage implements OnInit {
  currentUser: string | null = null;
  contacts: Contact[] = [];
  searchTerm = '';
  isLoading = false;
  isDark = false;
  readonly skeletonItems = [1, 2, 3, 4];

  constructor(
    private auth: AuthService,
    private contactService: ContactService,
    private toastCtrl: ToastController,
    private alertCtrl: AlertController,
    private router: Router
  ) {}

  ngOnInit() {
    const saved = localStorage.getItem('theme');
    this.isDark = saved === 'dark' ||
      (saved === null && window.matchMedia('(prefers-color-scheme: dark)').matches);
    document.body.classList.toggle('ion-palette-dark', this.isDark);

    this.currentUser = this.auth.getCurrentUser();
    if (!this.currentUser) {
      this.router.navigateByUrl('/home', { replaceUrl: true });
      return;
    }
    this.loadContacts();
  }

  toggleTheme() {
    this.isDark = !this.isDark;
    localStorage.setItem('theme', this.isDark ? 'dark' : 'light');
    this.applyTheme();
  }

  private applyTheme() {
    document.body.classList.toggle('ion-palette-dark', this.isDark);
  }

  ionViewWillEnter() {
    if (this.currentUser) {
      this.loadContacts();
    }
  }

  async loadContacts() {
    if (!this.currentUser) return;
    this.isLoading = true;
    try {
      const response = await firstValueFrom(this.contactService.getContacts(this.currentUser));
      if (response.state) {
        this.contacts = response.contacts ?? [];
      } else {
        await this.presentToast(response.message || 'No se pudieron cargar los contactos');
      }
    } catch (error: any) {
      await this.presentToast(error?.message || 'Error al cargar contactos');
    } finally {
      this.isLoading = false;
    }
  }

  get filteredContacts(): Contact[] {
    const term = this.searchTerm.trim().toLowerCase();
    if (!term) return this.contacts;
    return this.contacts.filter(c =>
      c.nombre.toLowerCase().includes(term) ||
      c.telefono.includes(term) ||
      c.email.toLowerCase().includes(term)
    );
  }

  onSearch(event: any) {
    this.searchTerm = event.detail.value ?? '';
  }

  async goToEdit(contact: Contact) {
    await this.router.navigate(['/actualizar-contacto'], { state: { contact } });
  }

  async confirmDelete(contact: Contact) {
    const alert = await this.alertCtrl.create({
      header: 'Eliminar contacto',
      message: `¿Estás seguro de que deseas eliminar a <strong>${contact.nombre}</strong>? Esta acción no se puede deshacer.`,
      buttons: [
        { text: 'Cancelar', role: 'cancel' },
        {
          text: 'Eliminar',
          role: 'destructive',
          cssClass: 'alert-btn-danger',
          handler: () => this.deleteContact(contact),
        },
      ],
    });
    await alert.present();
  }

  private async deleteContact(contact: Contact) {
    try {
      const response = await firstValueFrom(
        this.contactService.deleteContact(contact.id, this.currentUser!)
      );
      await this.presentToast(response.message);
      if (response.state) {
        this.contacts = this.contacts.filter(c => c.id !== contact.id);
      }
    } catch (error: any) {
      await this.presentToast(error?.message || 'Error al eliminar el contacto');
    }
  }

  async logout() {
    this.auth.logout();
    await this.router.navigateByUrl('/home', { replaceUrl: true });
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
