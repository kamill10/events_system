import { useAtom } from "jotai";
import { atomWithStorage } from 'jotai/utils';

type NullableString = string | null;

const token = atomWithStorage<NullableString>('token', null);

export const useToken = () => {
    const [tokenValue, setToken] = useAtom(token);
    return [tokenValue, setToken] as const;
}

const etag = atomWithStorage<NullableString>('etag', null);

export const useEtag = () => {
    const [etagValue, setEtag] = useAtom(etag);
    return [etagValue, setEtag] as const;
}