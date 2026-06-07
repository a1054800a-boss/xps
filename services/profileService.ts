// Powered by OnSpace.AI
import AsyncStorage from '@react-native-async-storage/async-storage';
import { STORAGE_KEY } from '@/constants/config';

export interface SpoofProfile {
  id:           string;
  name:         string;
  targetPackage:string;
  enabled:      boolean;
  createdAt:    number;

  // Network
  ipv4:         string;
  ipv6:         string;

  // Device
  manufacturer: string;
  model:        string;
  androidId:    string;
  fingerprint:  string;
  brand:        string;
  device:       string;
  product:      string;
  buildId:      string;
  sdkInt:       string;
}

export const defaultProfile = (): Omit<SpoofProfile, 'id' | 'name' | 'createdAt'> => ({
  targetPackage: '',
  enabled:       true,
  ipv4:          '192.168.1.100',
  ipv6:          'fe80::1',
  manufacturer:  'Google',
  model:         'Pixel 7',
  androidId:     'a1b2c3d4e5f6a7b8',
  fingerprint:   'google/redfin/redfin:12/SQ3A.220705.003/8672230:user/release-keys',
  brand:         'google',
  device:        'redfin',
  product:       'redfin',
  buildId:       'SQ3A.220705.003',
  sdkInt:        '32',
});

export async function loadProfiles(): Promise<SpoofProfile[]> {
  try {
    const raw = await AsyncStorage.getItem(STORAGE_KEY);
    if (!raw) return [];
    return JSON.parse(raw) as SpoofProfile[];
  } catch {
    return [];
  }
}

export async function saveProfiles(profiles: SpoofProfile[]): Promise<void> {
  await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(profiles));
}

export async function addProfile(profile: SpoofProfile): Promise<void> {
  const list = await loadProfiles();
  list.push(profile);
  await saveProfiles(list);
}

export async function updateProfile(updated: SpoofProfile): Promise<void> {
  const list = await loadProfiles();
  const idx  = list.findIndex(p => p.id === updated.id);
  if (idx !== -1) {
    list[idx] = updated;
    await saveProfiles(list);
  }
}

export async function deleteProfile(id: string): Promise<void> {
  const list = await loadProfiles();
  await saveProfiles(list.filter(p => p.id !== id));
}
