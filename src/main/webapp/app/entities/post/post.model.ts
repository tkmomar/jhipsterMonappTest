import dayjs from 'dayjs/esm';
import { ICategory } from 'app/entities/category/category.model';
import { IAuthor } from 'app/entities/author/author.model';

export interface IPost {
  id: number;
  title?: string | null;
  content?: string | null;
  dateCreation?: dayjs.Dayjs | null;
  category?: Pick<ICategory, 'id'> | null;
  author?: Pick<IAuthor, 'id'> | null;
}

export type NewPost = Omit<IPost, 'id'> & { id: null };
