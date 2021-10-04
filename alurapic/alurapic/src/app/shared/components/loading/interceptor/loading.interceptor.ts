import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { LoadingService } from '../service/loading.service';

@Injectable({ providedIn: 'root'})
export class LoadingInterceptor implements HttpInterceptor {

    constructor(
        private loadingService: LoadingService
    ) {}

    intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
        return next
            .handle(request)
            .pipe(tap(event => {
                if(event instanceof HttpResponse) {
                    this.loadingService.stop();
                } else {
                    this.loadingService.start();
                }
            }))
    }
}
