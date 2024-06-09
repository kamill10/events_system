import {PaginationResponse} from "./Pagination.ts";


export interface Speaker {
  id: string;
  firstName: string;
  lastName: string;
}
export interface PaginationSpeakerResponse
    extends PaginationResponse<Speaker> {}

export interface SpeakersWithNumberOfElements {
  speakers: Speaker[];
  totalElements: number;
}
export interface CreateSpeaker {
  firstName: string;
  lastName: string;
}

export interface UpdateSpeakerDataType {
  firstName: string;
  lastName: string;
}
