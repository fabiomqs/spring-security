import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenService } from '../jwt-token/token.service';
import { HeaderType } from 'src/app/enums/header-type.enum';

@Injectable()
export class RequestInterceptor implements HttpInterceptor {

    constructor(private tokenService: TokenService) {}

    intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
        if(this.tokenService.hasToken()) {
            const token = HeaderType.BEARER + this.tokenService.getToken();
            request = request.clone({
                setHeaders: {
                    'Authorization': token
                }
            })
        }
        return next.handle(request);
    }
}
