import axios from 'axios';

export class PhotoService {

    getImages() {
        return axios.get(process.env.PUBLIC_URL +'/showcase/demo/data/photos.json')
                .then(res => res.data.data);
    }
} 
