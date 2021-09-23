import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticationService } from 'src/app/service/authenticattion.service';
import { UserService } from 'src/app/service/user.service';
import { HeaderType } from 'src/app/enums/header-type.enum';
import { EnumRoutes } from 'src/app/enums/enum-routes.enum';

@Injectable()
export class RequestInterceptor implements HttpInterceptor {

    constructor(
        private authenticattionService: AuthenticationService,
        private userService: UserService) {}

    //exclude
    // /login, /api/v1/user/register, /api/v1/user/resetpassword/**, /api/v1/user/image/**
    intercept(request: HttpRequest<any>, handler: HttpHandler): Observable<HttpEvent<any>> {
        if(request.url.includes(`${this.authenticattionService.host}${this.authenticattionService.loginPath}`) || 
           request.url.includes(`${this.userService.host}${this.userService.apiPrefix}
           ${this.userService.userPath}/${EnumRoutes.REGISTER}`) ||
           request.url.includes(`${this.userService.host}${this.userService.apiPrefix}
           ${this.userService.userPath}/${EnumRoutes.RESET_PASSWORD}`) || 
           request.url.includes(`${this.userService.host}${this.userService.apiPrefix}
           ${this.userService.userPath}/${EnumRoutes.IMAGE}`)) {
            
            return handler.handle(request);

        }
        this.authenticattionService.loadToken();
        const token = this.authenticattionService.getToken();
        const cloneRequest = request.clone({ setHeaders: { Authorization: `${HeaderType.BEARER}${token}` } });
        return handler.handle(cloneRequest);
    }
}
