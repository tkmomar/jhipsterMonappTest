import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 12397,
  login: 'f.8p',
};

export const sampleWithPartialData: IUser = {
  id: 23829,
  login: '8PCrh@1B\\=d7m\\g5VGZ\\jhWHcR',
};

export const sampleWithFullData: IUser = {
  id: 4144,
  login: 'G7',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
