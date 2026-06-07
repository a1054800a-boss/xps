// Powered by OnSpace.AI
export const STORAGE_KEY = 'xspoof_profiles';
export const PREFS_KEY   = 'xspoof_active';

export const MANUFACTURERS = [
  'Samsung', 'Google', 'OnePlus', 'Xiaomi', 'Huawei',
  'Sony', 'Motorola', 'LG', 'HTC', 'Asus',
];

export const MODELS: Record<string, string[]> = {
  Samsung:   ['SM-G991B', 'SM-S918B', 'SM-A546B', 'SM-G998B'],
  Google:    ['Pixel 8', 'Pixel 8 Pro', 'Pixel 7', 'Pixel 6'],
  OnePlus:   ['LE2115', 'PHB110', 'IV2201', 'NE2215'],
  Xiaomi:    ['2312DRA50G', '23049PCD8G', '21081111RG', '2201117TG'],
  Huawei:    ['ELS-NX9', 'NOH-NX9', 'CLT-AL01', 'ANA-NX9'],
  Sony:      ['XQ-BT52', 'XQ-CT54', 'XQ-AS52', 'XQ-BC72'],
  Motorola:  ['XT2303-2', 'XT2201-1', 'XT2153-1', 'XT2175-2'],
  LG:        ['LM-G900', 'LM-V600', 'LM-G850', 'LM-Q730'],
  HTC:       ['2Q7A100', '2Q8H100', '2PZC5', 'HTC_U20_5G'],
  Asus:      ['ASUS_I006D', 'ASUS_I004D', 'ASUS_I002A', 'ASUS_AI2203'],
};

export const ANDROID_VERSIONS: Record<string, string> = {
  '10': 'Q',
  '11': 'R',
  '12': 'S',
  '13': 'T',
  '14': 'U',
};
