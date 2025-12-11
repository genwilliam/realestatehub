package com.example.realestatehub.service.impl;

import com.example.realestatehub.common.exception.BusinessException;
import com.example.realestatehub.dto.house.HouseCreateRequest;
import com.example.realestatehub.dto.house.HouseQuery;
import com.example.realestatehub.dto.house.HouseResponse;
import com.example.realestatehub.dto.house.HouseUpdateRequest;
import com.example.realestatehub.model.entity.Agent;
import com.example.realestatehub.model.entity.House;
import com.example.realestatehub.model.entity.HouseImage;
import com.example.realestatehub.model.entity.HouseTag;
import com.example.realestatehub.model.entity.User;
import com.example.realestatehub.model.enums.HouseStatus;
import com.example.realestatehub.model.enums.Role;
import com.example.realestatehub.repository.AgentRepository;
import com.example.realestatehub.repository.HouseRepository;
import com.example.realestatehub.repository.UserRepository;
import com.example.realestatehub.service.HouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;
    private final AgentRepository agentRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public HouseResponse create(HouseCreateRequest request, Long agentUserId) {
        Agent agent = getAgent(agentUserId);
        House house = buildHouseFromRequest(new House(), request, agent);
        house.setStatus(HouseStatus.DRAFT);
        houseRepository.save(house);
        return toResponse(house);
    }

    @Override
    @Transactional
    public HouseResponse update(Long id, HouseUpdateRequest request, Long agentUserId) {
        House house = houseRepository.findById(id)
                .orElseThrow(() -> new BusinessException("房源不存在"));
        Agent agent = getAgent(agentUserId);
        if (!house.getAgent().getId().equals(agent.getId())) {
            throw new BusinessException("无权操作该房源");
        }
        buildHouseFromUpdate(house, request);
        houseRepository.save(house);
        return toResponse(house);
    }

    @Override
    @Transactional
    public void delete(Long id, Long agentUserId) {
        House house = houseRepository.findById(id)
                .orElseThrow(() -> new BusinessException("房源不存在"));
        Agent agent = getAgent(agentUserId);
        if (!house.getAgent().getId().equals(agent.getId())) {
            throw new BusinessException("无权操作该房源");
        }
        houseRepository.delete(house);
    }

    @Override
    @Transactional(readOnly = true)
    public HouseResponse findById(Long id) {
        House house = houseRepository.findById(id)
                .orElseThrow(() -> new BusinessException("房源不存在"));
        return toResponse(house);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HouseResponse> search(HouseQuery query) {
        Sort sort = Sort.by(query.getDirection(), query.getSortBy());
        PageRequest pageRequest = PageRequest.of(query.getPage(), query.getSize(), sort);
        Page<House> page = houseRepository.search(query.getRegion(), query.getStatus(),
                query.getMinPrice(), query.getMaxPrice(), query.getLayout(), pageRequest);
        return page.map(this::toResponse);
    }

    private Agent getAgent(Long agentUserId) {
        User user = userRepository.findById(agentUserId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        if (user.getRole() != Role.AGENT) {
            throw new BusinessException("当前用户不是经纪人");
        }
        return agentRepository.findByUser(user)
                .orElseThrow(() -> new BusinessException("经纪人资料不存在"));
    }

    private House buildHouseFromRequest(House house, HouseCreateRequest request, Agent agent) {
        house.setTitle(request.getTitle());
        house.setDescription(request.getDescription());
        house.setPrice(request.getPrice());
        house.setArea(request.getArea());
        house.setRegion(request.getRegion());
        house.setAddress(request.getAddress());
        house.setLayout(request.getLayout());
        house.setAgent(agent);
        house.setApproved(false);
        house.setPublishTime(LocalDateTime.now());
        rebuildTagsAndImages(house, request.getTags(), request.getImages());
        return house;
    }

    private void buildHouseFromUpdate(House house, HouseUpdateRequest request) {
        if (request.getTitle() != null) house.setTitle(request.getTitle());
        if (request.getDescription() != null) house.setDescription(request.getDescription());
        if (request.getPrice() != null) house.setPrice(request.getPrice());
        if (request.getArea() != null) house.setArea(request.getArea());
        if (request.getRegion() != null) house.setRegion(request.getRegion());
        if (request.getAddress() != null) house.setAddress(request.getAddress());
        if (request.getLayout() != null) house.setLayout(request.getLayout());
        if (request.getTags() != null || request.getImages() != null) {
            rebuildTagsAndImages(house, request.getTags(), request.getImages());
        }
    }

    private void rebuildTagsAndImages(House house, List<String> tags, List<String> images) {
        house.getTags().clear();
        if (tags != null) {
            List<HouseTag> tagEntities = tags.stream().map(t -> {
                HouseTag tag = new HouseTag();
                tag.setHouse(house);
                tag.setTag(t);
                return tag;
            }).collect(Collectors.toList());
            house.getTags().addAll(tagEntities);
        }
        house.getImages().clear();
        if (images != null) {
            List<HouseImage> imageEntities = images.stream().map(url -> {
                HouseImage img = new HouseImage();
                img.setHouse(house);
                img.setUrl(url);
                return img;
            }).collect(Collectors.toList());
            house.getImages().addAll(imageEntities);
        }
    }

    private HouseResponse toResponse(House house) {
        return HouseResponse.builder()
                .id(house.getId())
                .title(house.getTitle())
                .description(house.getDescription())
                .price(house.getPrice())
                .area(house.getArea())
                .region(house.getRegion())
                .address(house.getAddress())
                .layout(house.getLayout())
                .status(house.getStatus())
                .approved(house.isApproved())
                .publishTime(house.getPublishTime())
                .viewCount(house.getViewCount())
                .images(house.getImages().stream().map(HouseImage::getUrl).toList())
                .tags(house.getTags().stream().map(HouseTag::getTag).toList())
                .agentName(house.getAgent() != null && house.getAgent().getUser() != null ? house.getAgent().getUser().getName() : null)
                .agentPhone(house.getAgent() != null && house.getAgent().getUser() != null ? house.getAgent().getUser().getPhone() : null)
                .build();
    }
}

