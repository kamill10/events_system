import { useAtom } from "jotai";
import { atomWithStorage } from 'jotai/utils';

type NullableString = string | null;

export const atomToken = atomWithStorage<NullableString>('token', null);

export const atomEtag = atomWithStorage<NullableString>('etag', null);
