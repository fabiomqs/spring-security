import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Photo } from './photo';

const API = 'http://localhost:8080';

@Injectable({
  providedIn: 'root'
})
export class PhotoService {

    constructor(private http:HttpClient) { }

    listFromUser(username: string):Observable<Photo[]> {
        return this.http
                .get<Photo[]>(`${API}/api/v1/photos/${username}`)
    }

    listFromUserPaginated(username: string, page: number):Observable<Photo[]> {
        const params = new HttpParams()
                .append('page', page.toString());
        return this.http
                .get<Photo[]>(`${API}/api/v1/photos/${username}`, { params })
    }

}
