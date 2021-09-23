import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EnumRoutes } from './enums/enum-routes.enum';
import { LoginComponent } from './home/login/login.component';
import { RegisterComponent } from './home/register/register.component';
import { UserComponent } from './users/user/user.component';

const routes: Routes = [
    {path: EnumRoutes.LOGIN, component: LoginComponent},
    {path: EnumRoutes.REGISTER, component: RegisterComponent},
    {path: EnumRoutes.USERS, component: UserComponent},
    {path: '**', redirectTo: EnumRoutes.LOGIN, pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
