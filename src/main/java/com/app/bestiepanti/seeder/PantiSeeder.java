package com.app.bestiepanti.seeder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.app.bestiepanti.model.Panti;
import com.app.bestiepanti.model.Role;
import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.repository.PantiRepository;
import com.app.bestiepanti.repository.RoleRepository;
import com.app.bestiepanti.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PantiSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PantiRepository pantiRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Role role = roleRepository.findByName("ROLE_PANTI");

        for (int i = 1; i <= 10; i++) {
            if (userRepository.findByEmail("panti" + i + "@gmail.com").isEmpty()) {
                UserApp user = new UserApp();
                user.setName("Panti" + i);
                user.setEmail("panti" + i + "@gmail.com");
                user.setPassword(passwordEncoder.encode("123123"));
                user.setRole(role);
                userRepository.save(user);

                Panti panti = new Panti();
                panti.setAddress("Jl. Apel " + i);
                panti.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum tristique neque vitae porttitor dignissim. Nullam rhoncus faucibus neque quis vehicula. Donec velit mauris, suscipit ut pharetra quis, mattis a lectus. Proin ut lectus eros. Integer mauris odio, pharetra sed rutrum quis, dignissim et sem. Aliquam fringilla, tellus sed fringilla facilisis, justo nunc volutpat velit, vel ultricies justo purus faucibus ligula. Sed blandit laoreet scelerisque. Nulla eu convallis tortor. Nulla ac venenatis tortor. In sit amet erat tortor. Pellentesque varius egestas euismod. Duis laoreet ornare turpis nec convallis. Proin semper lorem eu turpis pretium, non consequat metus tristique. Morbi fermentum dignissim enim rutrum ornare. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.");
                List<String> donationTypes = Arrays.asList("Dana","Pangan","Barang","Tenaga");
                List<String> pickedDonationTypes = pickRandomDonationTypes(donationTypes);
                panti.setDonationTypes(pickedDonationTypes);
                List<String> images = Arrays.asList("test" + i + ".png");
                panti.setImage(images);
                List<Integer> isUrgentTypes = Arrays.asList(1,0);
                Integer pickedIsUrgent = pickIsUrgent(isUrgentTypes);
                panti.setIsUrgent(pickedIsUrgent);
                panti.setPhone("08123123123" + i);
                panti.setQris("test" + i + ".png");
                List<String> regions = Arrays.asList("Jakarta","Bogor","Depok","Tanggerang","Bekasi");
                String pickedRegion = pickRandomRegion(regions);
                panti.setRegion(pickedRegion);
                panti.setUser(user);
                pantiRepository.save(panti);
            }
        }
    }

    public static Integer pickIsUrgent(List<Integer> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    public static String pickRandomRegion(List<String> regions) {
        Random random = new Random();
        int randomIndex = random.nextInt(regions.size()); // Generate a random index
        return regions.get(randomIndex); // Return the randomly picked region
    }
    
    public static List<String> pickRandomDonationTypes(List<String> donationTypes) {
        Random random = new Random();
        int numberOfItemsToPick = random.nextInt(donationTypes.size()) + 1; 
        
        List<Integer> randomIndices = IntStream.range(0, donationTypes.size())
                                                .boxed()
                                                .collect(Collectors.toList());
        Collections.shuffle(randomIndices, random);
        return randomIndices.stream()
                            .limit(numberOfItemsToPick)
                            .map(donationTypes::get)
                            .collect(Collectors.toList());
    }
}
