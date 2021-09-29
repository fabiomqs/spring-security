import { HttpClient, HttpParams } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { APIURL } from 'src/app/core/tokens';
import { Photo } from './photo';

@Injectable({
  providedIn: 'root'
})
export class PhotoService {

    constructor(
        private http:HttpClient,
        @Inject(APIURL) private apiUrl: string,
        
    ) { }

    listFromUser(username: string):Observable<Photo[]> {
        return this.http
                .get<Photo[]>(`${this.apiUrl}/api/v1/photos/${username}`)
    }

    listFromUserPaginated(username: string, page: number, size:number):Observable<Photo[]> {
        const params = new HttpParams()
                .append('page', page.toString())
                .append('size', size.toString());
        return this.http
                .get<Photo[]>(`${this.apiUrl}/api/v1/photos/${username}`, { params })
    }

}
