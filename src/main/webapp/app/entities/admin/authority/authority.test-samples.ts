import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'dd7af8ea-6284-45ab-8ee5-1eaddeed339a',
};

export const sampleWithPartialData: IAuthority = {
  name: 'a441932b-ed8f-4c7d-8e82-646c4aa7b446',
};

export const sampleWithFullData: IAuthority = {
  name: 'a746d3b5-2636-47c1-b195-594f63d252c3',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
